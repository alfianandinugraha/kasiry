<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Wholesale extends Model
{
    use HasFactory;

    protected $fillable = ["wholesale_id", "quantity", "sell_price"];

    protected $primaryKey = "wholesale_id";

    public $keyType = "string";

    public $incrementing = false;

    public function product()
    {
        return $this->belongsTo(Product::class, "product_id", "product_id");
    }
}
