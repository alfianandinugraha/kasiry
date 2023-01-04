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
        "category_id",
    ];

    protected $primaryKey = "product_id";

    public $keyType = "string";

    public $incrementing = false;

    public function company()
    {
        return $this->belongsTo(Company::class, "company_id", "company_id");
    }

    public function category()
    {
        return $this->belongsTo(Category::class, "category_id", "category_id");
    }
}
