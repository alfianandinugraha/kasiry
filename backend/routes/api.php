<?php

use App\Http\Controllers\AuthController;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\EmployeeController;
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
});
