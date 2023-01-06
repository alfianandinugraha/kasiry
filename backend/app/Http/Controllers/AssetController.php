<?php

namespace App\Http\Controllers;

use App\Models\Asset;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Str;

class AssetController extends Controller
{
    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            "fileName" => ["required", "string", "max:255"],
            "file" => ["required", "file", "max:5000"],
        ]);

        $validator->validate();

        $extension = $request->file->extension();
        $assetId = Str::random(10);
        $assetName = "$assetId.$extension";
        $user = $request->user();

        $request->file->move(storage_path("app/public"), $assetName);

        $value = [
            "asset_id" => $assetId,
            "file_name" => $request->fileName,
            "extension" => $extension,
            "company_id" => $user->company_id,
        ];

        $asset = new Asset($value);
        $asset->saveOrFail();

        return Response::make(
            [
                "message" => "Berhasil mengupload file.",
                "data" => collect($asset)
                    ->camelKeys()
                    ->all(),
            ],
            201
        );
    }
}
