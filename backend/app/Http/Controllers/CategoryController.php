<?php

namespace App\Http\Controllers;

use App\Models\Category;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class CategoryController extends Controller
{
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
        ]);

        $validator->validate();

        $user = $request->user();
        $categoryId = Str::random(10);
        $values = [
            "category_id" => $categoryId,
            "company_id" => $user->company_id,
            "name" => $request->name,
        ];
        $category = new Category($values);

        $category->saveOrFail();

        return Response::make([
            "message" => "Berhasil ditambahkan",
            "data" => collect($values)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function index(Request $request)
    {
        $user = $request->user();
        $categories = Category::query()
            ->where("company_id", $user->company_id)
            ->get();

        return Response::make([
            "data" => collect($categories)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function detail(Request $request, $categoryId)
    {
        $user = $request->user();
        $category = Category::query()
            ->where("company_id", $user->company_id)
            ->where("category_id", $categoryId)
            ->firstOrFail();

        return Response::make([
            "data" => collect($category)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function update(Request $request, $categoryId)
    {
        $validator = Validator::make($request->all(), [
            "name" => ["required", "string", "max:255"],
        ]);

        $validator->validate();

        $user = $request->user();
        $values = [
            "name" => $request->name,
            "category_id" => $categoryId,
            "company_id" => $user->company_id,
        ];

        Category::query()
            ->find($categoryId)
            ->updateOrFail($values);

        return Response::make([
            "message" => "Berhasil diubah",
            "data" => collect($values)
                ->camelKeys()
                ->all(),
        ]);
    }

    public function delete(Request $request, $categoryId)
    {
        $user = $request->user();
        $category = Category::query()
            ->where("company_id", $user->company_id)
            ->where("category_id", $categoryId)
            ->firstOrFail();

        $category->delete();

        return Response::make([
            "message" => "Berhasil dihapus",
            "data" => [
                "categoryId" => $categoryId,
            ],
        ]);
    }
}
