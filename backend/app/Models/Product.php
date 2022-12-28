<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Product extends Model
{
    use HasFactory;

    protected $fillable = [
        "product_id",
        "name",
        "description",
        "price",
        "stock",
        "buy_price",
        "sell_price",
        "barcode",
        "company_id",
        "weight_id",
        "category_id",
        "wholesale_id",
    ];

    protected $primaryKey = "product_id";

    public $keyType = "string";

    public $incrementing = false;
}
