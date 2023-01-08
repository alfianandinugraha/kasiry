<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Casts\Attribute;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\URL;

class Asset extends Model
{
    use HasFactory;

    protected $fillable = ["asset_id", "file_name", "extension", "company_id"];

    protected $primaryKey = "asset_id";

    public $keyType = "string";

    public $incrementing = false;

    protected $appends = ["url"];

    public function url(): Attribute
    {
        $host = request()->getHost();
        $port = request()->getPort();

        return Attribute::make(
            get: fn() => "http://$host:$port/api/assets/$this->asset_id"
        );
    }

    public function product()
    {
        return $this->hasOne(Product::class, "picture_id", "asset_id");
    }

    public function transactionProducts()
    {
        return $this->hasOne(
            TransactionProduct::class,
            "picture_id",
            "asset_id"
        );
    }
}
