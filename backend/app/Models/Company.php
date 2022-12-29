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

    public function users()
    {
        return $this->hasMany(User::class, "user_id", "user_id");
    }

    public function payments()
    {
        return $this->hasMany(Payment::class, "company_id", "company_id");
    }

    public function products()
    {
        return $this->hasMany(Product::class, "company_id", "company_id");
    }

    public function categories()
    {
        return $this->hasMany(Category::class, "company_id", "company_id");
    }
}
