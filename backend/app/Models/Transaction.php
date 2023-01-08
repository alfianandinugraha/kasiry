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

    public function company()
    {
        return $this->belongsTo(Company::class, "company_id", "company_id");
    }

    public function products()
    {
        return $this->hasMany(
            TransactionProduct::class,
            "transaction_id",
            "transaction_id"
        );
    }
}
