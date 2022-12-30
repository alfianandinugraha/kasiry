<?php

namespace App\Http\Controllers;

use App\Models\Company;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class CompanyController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();
        $company = Company::query()->find($user->company_id);

        return Response::make([
            "data" => collect($company)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function store(Request $request)
    {
        $user = $request->user();
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
            "address" => ["required", "string", "max:255"],
            "phone" => ["required", "string", "max:255"],
        ]);

        $validator->after(function ($validator) use ($user) {
            if ($user->company_id) {
                $validator->errors()->add("name", "Kamu sudah memiliki usaha.");
            }
        });

        $validator->validate();

        $companyId = Str::random(10);
        $company = new Company([
            "company_id" => $companyId,
            "name" => $request->name,
            "address" => $request->address,
            "phone" => $request->phone,
        ]);
        $user = User::query()->find($user->user_id);

        DB::transaction(function () use ($company, $user, $companyId) {
            $company->saveOrFail();
            $user->updateOrFail([
                "company_id" => $companyId,
            ]);
        });

        return Response::make(
            [
                "message" => "Berhasil membuat usaha.",
                "data" => collect($company)
                    ->camelKeys()
                    ->all(),
            ],
            201
        );
    }

    public function update(Request $request)
    {
        $user = $request->user();
        $validator = Validator::make($request->all(), [
            "name" => ["string", "max:255"],
            "address" => ["string", "max:255"],
            "phone" => ["string", "max:255"],
        ]);

        $validator->after(function ($validator) use ($user) {
            if (!$user->company_id) {
                $validator->errors()->add("name", "Kamu belum memiliki usaha.");
            }
        });

        $validator->validate();

        Company::query()
            ->find($user->company_id)
            ->updateOrFail([
                "name" => $request->name,
                "address" => $request->address,
                "phone" => $request->phone,
            ]);

        return Response::make([
            "message" => "Berhasil mengubah usaha.",
        ]);
    }
}
