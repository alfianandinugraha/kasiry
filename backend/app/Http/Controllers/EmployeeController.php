<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;
use Illuminate\Validation\Rule;

class EmployeeController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $users = User::query()
            ->where("company_id", $user->company_id)
            ->get();

        return Response::make([
            "data" => collect($users)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function detail(Request $request, $userId)
    {
        $user = $request->user();
        $user = User::query()
            ->where("company_id", $user->company_id)
            ->where("user_id", $userId)
            ->firstOrFail();

        return Response::make([
            "data" => collect($user)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function delete(Request $request, $userId)
    {
        $user = $request->user();
        $user = User::query()
            ->where("company_id", $user->company_id)
            ->where("user_id", $userId)
            ->firstOrFail();

        $user->delete();

        return Response::make([
            "data" => collect($user)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function store(Request $request)
    {
        $user = $request->user();
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
            "email" => [
                "required",
                "string",
                "email",
                "max:255",
                "unique:users,email",
            ],
            "phone" => ["required", "string", "max:255"],
            "abilities" => ["required"],
            "password" => ["required", "string", "min:8"],
        ]);

        $validator->validate();

        $userId = Str::random(10);
        $newUser = new User([
            "user_id" => $userId,
            "name" => $request->name,
            "email" => $request->email,
            "phone" => $request->phone,
            "company_id" => $user->company_id,
            "abilities" => $request->abilities,
            "password" => Hash::make($request->password),
        ]);

        $newUser->saveOrFail();

        return Response::make(
            [
                "data" => collect($newUser)
                    ->camelKeys()
                    ->all(),
            ],
            201
        );
    }

    public function update(Request $request, $userId)
    {
        $user = User::query()
            ->where("company_id", $request->user()->company_id)
            ->where("user_id", $userId)
            ->firstOrFail();

        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
            "email" => [
                "required",
                "string",
                "email",
                "max:255",
                Rule::unique("users", "email")->ignore($userId, "user_id"),
            ],
            "phone" => ["required", "string", "max:255"],
            "abilities" => ["required", "array"],
            "password" => ["required", "string", "min:8"],
        ]);

        $validator->validate();

        $user->updateOrFail([
            "name" => $request->name,
            "email" => $request->email,
            "phone" => $request->phone,
            "abilities" => $request->abilities,
            "password" => Hash::make($request->password),
        ]);

        return Response::make([
            "data" => collect($user)
                ->camelKeys()
                ->all(),
        ]);
    }
}
