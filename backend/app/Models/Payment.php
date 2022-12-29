<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Payment extends Model
{
    use HasFactory;

    protected $fillable = ["payment_id", "name", "company_id"];

    protected $primaryKey = "payment_id";

    public $keyType = "string";

    public $incrementing = false;

    public function company()
    {
        return $this->belongsTo(Company::class, "company_id", "company_id");
    }
}
