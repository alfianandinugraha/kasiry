<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Company extends Model
{
    use HasFactory;

    protected $fillable = [
        "company_id",
        "name",
        "email",
        "address",
        "phone",
        "user_id",
    ];

    protected $primaryKey = "company_id";

    public $keyType = "string";

    public $incrementing = false;
}
