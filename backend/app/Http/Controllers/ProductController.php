<?php

namespace App\Http\Controllers;

use App\Models\Product;
use GuzzleHttp\Handler\Proxy;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class ProductController extends Controller
{
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
            "description" => ["string", "max:255"],
            "stock" => ["required", "numeric", "min:0"],
            "buyPrice" => ["required", "numeric", "min:0"],
            "sellPrice" => ["required", "numeric", "min:0"],
            "barcode" => ["nullable", "string", "max:255"],
            "categoryId" => [
                "nullable",
                "string",
                "exists:categories,category_id",
            ],
            "pictureId" => ["nullable", "string", "exists:assets,asset_id"],
        ]);

        $validator->validate();

        $user = $request->user();
        $productId = Str::random(10);
        $values = [
            "product_id" => $productId,
            "name" => $request->name,
            "description" => $request->description,
            "stock" => $request->stock,
            "buy_price" => $request->buyPrice,
            "sell_price" => $request->sellPrice,
            "barcode" => $request->barcode,
            "company_id" => $user->company_id,
            "category_id" => $request->categoryId,
            "picture_id" => $request->pictureId,
        ];

        $product = new Product($values);
        $product->saveOrFail();

        $product = Product::query()->find($productId);
        $product->asset;
        $product->category;
        $product->company;

        return Response::make(
            [
                "message" => "Berhasil menambahkan produk.",
                "data" => collect($product)
                    ->camelKeys()
                    ->all(),
            ],
            201
        );
    }

    public function delete(Request $request, $productId)
    {
        $user = $request->user();
        $product = Product::query()
            ->where("product_id", $productId)
            ->where("company_id", $user->company_id)
            ->firstOrFail();

        $product->deleteOrFail();

        return Response::make(
            [
                "message" => "Berhasil menghapus produk.",
                "data" => collect($product)
                    ->camelKeys()
                    ->all(),
            ],
            200
        );
    }

    public function detail(Request $request, $productId)
    {
        $user = $request->user();
        $product = Product::query()
            ->where("product_id", $productId)
            ->where("company_id", $user->company_id)
            ->firstOrFail();

        $product->picture;
        $product->category;
        $product->company;

        return Response::make(
            [
                "message" => "Berhasil mendapatkan detail produk.",
                "data" => collect($product)
                    ->camelKeys()
                    ->all(),
            ],
            200
        );
    }

    public function index(Request $request)
    {
        $user = $request->user();
        $limit = $request->query("limit");

        $products = Product::query()
            ->with(["picture", "category", "company"])
            ->where("company_id", $user->company_id)
            ->take($limit)
            ->get();

        foreach ($products as $product) {
            $product->picture;
            $product->category;
            $product->company;
        }

        return Response::make(
            [
                "message" => "Berhasil mendapatkan daftar produk.",
                "data" => collect($products)
                    ->camelKeys()
                    ->all(),
            ],
            200
        );
    }

    public function update(Request $request, $productId)
    {
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
            "description" => ["string", "max:255"],
            "stock" => ["required", "numeric", "min:0"],
            "buyPrice" => ["required", "numeric", "min:0"],
            "sellPrice" => ["required", "numeric", "min:0"],
            "barcode" => ["nullable", "string", "max:255"],
            "categoryId" => [
                "nullable",
                "string",
                "exists:categories,category_id",
            ],
            "pictureId" => ["nullable", "string", "exists:assets,asset_id"],
        ]);

        $validator->validate();

        $user = $request->user();
        $product = Product::query()
            ->where("product_id", $productId)
            ->where("company_id", $user->company_id)
            ->firstOrFail();

        $values = [
            "name" => $request->name,
            "description" => $request->description,
            "stock" => $request->stock,
            "buy_price" => $request->buyPrice,
            "sell_price" => $request->sellPrice,
            "barcode" => $request->barcode,
            "category_id" => $request->categoryId,
            "picture_id" => $request->pictureId,
        ];

        Product::query()
            ->find($productId)
            ->updateOrFail($values);

        $product->picture;
        $product->category;
        $product->company;

        return Response::make(
            [
                "message" => "Berhasil mengubah produk.",
                "data" => collect($product)
                    ->camelKeys()
                    ->all(),
            ],
            200
        );
    }
}
