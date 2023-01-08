<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class TransactionProduct extends Model
{
    use HasFactory;

    protected $fillable = [
        "transaction_product_id",
        "transaction_id",
        "name",
        "quantity",
        "buy_price",
        "sell_price",
        "barcode",
    ];

    protected $primaryKey = "transaction_product_id";

    public $keyType = "string";

    public $incrementing = false;

    public function transaction()
    {
        return $this->belongsTo(
            Transaction::class,
            "transaction_id",
            "transaction_id"
        );
    }
}
