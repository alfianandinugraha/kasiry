<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Asset extends Model
{
    use HasFactory;

    protected $fillable = ["asset_id", "file_name", "extension", "company_id"];

    protected $primaryKey = "asset_id";

    public $keyType = "string";

    public $incrementing = false;
}
