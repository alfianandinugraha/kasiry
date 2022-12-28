<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Transaction extends Model
{
    use HasFactory;

    protected $fillable = ["transaction_id", "company_id"];

    protected $primaryKey = "transaction_id";

    public $keyType = "string";

    public $incrementing = false;
}
