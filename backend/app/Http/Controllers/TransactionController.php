<?php

namespace App\Http\Controllers;

use App\Models\Product;
use App\Models\Transaction;
use App\Models\TransactionProduct;
use GuzzleHttp\Handler\Proxy;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class TransactionController extends Controller
{
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            "products" => ["required", "array"],
        ]);

        $validator->validate();

        $user = $request->user();
        $products = Product::query();

        foreach ($request->products as $product) {
            $products->orWhere("product_id", $product["productId"]);
        }

        $products->where("company_id", $user->company_id);
        $products = $products->get();

        $transactionId = Str::random(10);

        $products = collect($products)
            ->map(function ($product) use ($transactionId, $request) {
                $productRequest = collect($request->products)->firstWhere(
                    "productId",
                    $product->product_id
                );

                return [
                    "transaction_product_id" => Str::random(10),
                    "transaction_id" => $transactionId,
                    "name" => $product->name,
                    "quantity" => $productRequest["quantity"],
                    "buy_price" => $product->buy_price,
                    "sell_price" => $product->sell_price,
                    "barcode" => $product->barcode,
                    "product_id" => $product->product_id,
                    "picture_id" => $product->picture_id,
                ];
            })
            ->toArray();

        DB::transaction(function () use ($products, $transactionId, $user) {
            $transaction = new Transaction([
                "transaction_id" => $transactionId,
                "company_id" => $user->company_id,
            ]);

            $transaction->saveOrFail();

            TransactionProduct::query()->insert($products);

            foreach ($products as $product) {
                Product::query()
                    ->find($product["product_id"])
                    ->decrement("stock", $product["quantity"]);
            }
        });

        return Response::make(
            [
                "message" => "Berhasil menambahkan transaksi.",
                "data" => collect([
                    "transaction_id" => $transactionId,
                    "products" => $products,
                    "company_id" => $user->company_id,
                ])
                    ->camelKeys()
                    ->all(),
            ],
            201
        );
    }

    public function delete(Request $request, $transactionId)
    {
        $user = $request->user();

        DB::transaction(function () use ($transactionId, $user) {
            $transaction = Transaction::query()
                ->where("transaction_id", $transactionId)
                ->where("company_id", $user->company_id)
                ->firstOrFail();

            $transactionProducs = TransactionProduct::query()
                ->where("transaction_id", $transactionId)
                ->get();

            foreach ($transactionProducs as $transactionProduct) {
                Product::query()
                    ->find($transactionProduct->product_id)
                    ?->increment("stock", $transactionProduct->quantity);
            }

            $transaction->deleteOrFail();
        });

        return Response::make(
            [
                "message" => "Berhasil menghapus transaksi.",
                "data" => null,
            ],
            200
        );
    }

    public function index(Request $request)
    {
        $user = $request->user();
        $limit = $request->query("limit");

        $transactions = Transaction::query()
            ->with(["products", "products.picture"])
            ->where("company_id", $user->company_id)
            ->take($limit)
            ->get();

        foreach ($transactions as $transaction) {
            $products = $transaction->products;

            foreach ($products as $product) {
                $product->picture;
            }
        }

        return Response::make([
            "message" => "Berhasil",
            "data" => collect($transactions)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function detail(Request $request, $transactionId)
    {
        $user = $request->user();

        $transaction = Transaction::query()
            ->with(["products", "products.picture"])
            ->where("transaction_id", $transactionId)
            ->where("company_id", $user->company_id)
            ->firstOrFail();

        $products = $transaction->products;
        foreach ($products as $product) {
            $product->picture;
        }

        return Response::make([
            "message" => "Berhasil",
            "data" => collect($transaction)
                ->camelKeys()
                ->all(),
        ]);
    }
}
