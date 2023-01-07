<?php

use App\Http\Controllers\AssetController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\CategoryController;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\EmployeeController;
use App\Http\Controllers\ProductController;
use App\Http\Controllers\ProfileController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware("auth:sanctum")->get("/user", function (Request $request) {
    return $request->user();
});

Route::post("/login", [AuthController::class, "login"]);
Route::post("/register", [AuthController::class, "register"]);

Route::get("/assets/{assetId}", [AssetController::class, "detail"]);

Route::middleware("auth:sanctum")->group(function () {
    Route::post("/logout", [AuthController::class, "logout"]);

    Route::get("/profile", [ProfileController::class, "index"]);
    Route::put("/profile", [ProfileController::class, "update"]);

    Route::get("/company", [CompanyController::class, "index"]);
    Route::post("/company", [CompanyController::class, "store"]);
    Route::put("/company", [CompanyController::class, "update"]);

    Route::post("/employees", [EmployeeController::class, "store"]);
    Route::get("/employees", [EmployeeController::class, "index"]);
    Route::get("/employees/{userId}", [EmployeeController::class, "detail"]);
    Route::put("/employees/{userId}", [EmployeeController::class, "update"]);
    Route::delete("/employees/{userId}", [EmployeeController::class, "delete"]);

    Route::post("/categories", [CategoryController::class, "store"]);
    Route::get("/categories", [CategoryController::class, "index"]);
    Route::get("/categories/{categoryId}", [
        CategoryController::class,
        "detail",
    ]);
    Route::put("/categories/{categoryId}", [
        CategoryController::class,
        "update",
    ]);
    Route::delete("/categories/{categoryId}", [
        CategoryController::class,
        "delete",
    ]);

    Route::post("/assets", [AssetController::class, "store"]);

    Route::get("/products", [ProductController::class, "index"]);
    Route::post("/products", [ProductController::class, "store"]);
    Route::get("/products/{productId}", [ProductController::class, "detail"]);
    Route::put("/products/{productId}", [ProductController::class, "update"]);
    Route::delete("/products/{productId}", [
        ProductController::class,
        "delete",
    ]);
});
