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

    public const ADD_EMPLOYEE = "add-employee";
    public const UPDATE_EMPLOYEE = "update-employee";
    public const DELETE_EMPLOYEE = "delete-employee";
    public const READ_EMPLOYEE = "read-employee";

    public const ADD_COMPANY = "add-company";
    public const UPDATE_COMPANY = "update-company";
    public const DELETE_COMPANY = "delete-company";

    public const ADD_PRODUCT = "add-product";
    public const UPDATE_PRODUCT = "update-product";
    public const DELETE_PRODUCT = "delete-product";
    public const READ_PRODUCT = "read-product";

    public const ADD_CATEGORY = "add-category";
    public const UPDATE_CATEGORY = "update-category";
    public const DELETE_CATEGORY = "delete-category";
    public const READ_CATEGORY = "read-category";

    public const ADD_TRANSACTION = "add-transaction";
    public const UPDATE_TRANSACTION = "update-transaction";
    public const DELETE_TRANSACTION = "delete-transaction";
    public const READ_TRANSACTION = "read-transaction";

    public const ADD_WEIGHT = "add-weight";
    public const UPDATE_WEIGHT = "update-weight";
    public const DELETE_WEIGHT = "delete-weight";
    public const READ_WEIGHT = "read-weight";

    public const SUPER = "*";
}
