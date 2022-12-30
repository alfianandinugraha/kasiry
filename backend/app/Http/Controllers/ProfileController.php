<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;

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
}
