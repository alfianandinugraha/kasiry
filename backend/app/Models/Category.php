<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Category extends Model
{
    use HasFactory;

    protected $fillable = ["category_id", "name", "company_id"];

    protected $primaryKey = "category_id";

    public $keyType = "string";

    public $incrementing = false;
}
