<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Ability extends Model
{
    use HasFactory;

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

    public const LIST = [
        Ability::ADD_EMPLOYEE,
        Ability::UPDATE_EMPLOYEE,
        Ability::DELETE_EMPLOYEE,
        Ability::READ_EMPLOYEE,
        Ability::ADD_COMPANY,
        Ability::UPDATE_COMPANY,
        Ability::DELETE_COMPANY,
        Ability::ADD_PRODUCT,
        Ability::UPDATE_PRODUCT,
        Ability::DELETE_PRODUCT,
        Ability::READ_PRODUCT,
        Ability::ADD_CATEGORY,
        Ability::UPDATE_CATEGORY,
        Ability::DELETE_CATEGORY,
        Ability::READ_CATEGORY,
        Ability::ADD_TRANSACTION,
        Ability::UPDATE_TRANSACTION,
        Ability::DELETE_TRANSACTION,
        Ability::READ_TRANSACTION,
        Ability::ADD_WEIGHT,
        Ability::UPDATE_WEIGHT,
        Ability::DELETE_WEIGHT,
        Ability::READ_WEIGHT,
        Ability::SUPER,
    ];

    public static function exist($ability)
    {
        return collect(Ability::LIST)->contains($ability);
    }

    public static function allowed($requied, $abilities)
    {
        return collect($requied)->every(function ($ability) use ($abilities) {
            return collect($abilities)->contains($ability);
        });
    }

    public static function isSuper($abilities)
    {
        return collect($abilities)->contains(Ability::SUPER);
    }

    public static function middleware($abilities)
    {
        return "abilities:" . collect($abilities)->join("|");
    }
}
