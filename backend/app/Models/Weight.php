<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Weight extends Model
{
    use HasFactory;

    protected $fillable = ["weight_id", "name"];

    protected $primaryKey = "weight_id";

    public $keyType = "string";

    public $incrementing = false;
}
