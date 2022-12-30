<?php

namespace App\Http\Controllers;

use App\Models\Ability;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class AuthController extends Controller
{
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            "email" => ["required", "email", "exists:users,email"],
            "password" => ["required"],
        ]);

        $validator->validate();

        $user = User::where("email", $request->email)->firstOrFail();
        $isValid = Hash::check($request->password, $user->password);

        if (!$isValid) {
            return Response::json(
                [
                    "message" => "Password dan email tidak sesuai",
                ],
                401
            );
        }

        $expire = Carbon::now();
        $expire->addDays(7);
        $token = $user->createToken("access-token", [], $expire)
            ->plainTextToken;

        return Response::json([
            "message" => "Login successful",
            "data" => [
                "token" => $token,
            ],
        ]);
    }

    public function register(Request $request)
    {
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
            "phone" => ["required", "string", "max:255"],
            "email" => ["required", "email", "unique:users,email"],
            "password" => ["required", "min:8"],
        ]);

        $validator->validate();

        $userId = Str::random(10);
        $user = new User([
            "user_id" => $userId,
            "name" => $request->name,
            "phone" => $request->phone,
            "email" => $request->email,
            "password" => Hash::make($request->password),
            "abilities" => [Ability::SUPER],
        ]);

        $user->saveOrFail();

        return Response::json([
            "message" => "Registration successful",
        ]);
    }

    public function logout(Request $request)
    {
        $request
            ->user()
            ->currentAccessToken()
            ->delete();

        return Response::json([
            "message" => "Logout successful",
        ]);
    }
}
