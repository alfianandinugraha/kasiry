<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;

class ProfileController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $user->company;

        return Response::make([
            "data" => collect($user)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function update(Request $request)
    {
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
            "phone" => ["required", "string", "max:255"],
            "email" => ["required", "email"],
        ]);

        $validator->validate();

        $user = $request->user();
        $values = [
            "name" => $request->name,
            "phone" => $request->phone,
            "email" => $request->email,
            "user_id" => $user->user_id,
            "company_id" => $user->company_id,
            "abilities" => $user->abilities,
        ];

        User::query()
            ->find($user->user_id)
            ->updateOrFail($values);

        return Response::make([
            "message" => "Profile updated",
            "data" => collect($user)
                ->camelKeys()
                ->all(),
        ]);
    }
}
