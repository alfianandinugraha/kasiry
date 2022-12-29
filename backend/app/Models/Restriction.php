<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Restriction extends Model
{
    use HasFactory;

    protected $fillable = ["restriction_id", "allowed", "user_id"];

    protected $primaryKey = "restriction_id";

    public $keyType = "string";

    public $incrementing = false;

    public function user()
    {
        return $this->belongsTo(User::class, "user_id", "user_id");
    }
}
