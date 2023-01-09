<?php

namespace App\Http\Controllers;

use App\Models\Transaction;
use App\Models\TransactionProduct;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Response;

class SummaryController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $transactions = Transaction::query()
            ->with(["products"])
            ->where("company_id", $user->company_id)
            ->get(["transaction_id"]);

        $capital = 0;
        $profit = 0;
        $total = 0;

        foreach ($transactions as $transaction) {
            $products = $transaction->products;

            foreach ($products as $product) {
                Log::info($product->buyPrice);
                $capital += $product->buy_price * $product->quantity;
                $total += $product->sell_price * $product->quantity;
                $profit = $total - $capital;
            }
        }

        return Response::make([
            "message" => "Berhasil mendapatkan semua ringkasan",
            "data" => [
                "capital" => $capital,
                "profit" => $profit,
                "total" => $total,
            ],
        ]);
    }
}
