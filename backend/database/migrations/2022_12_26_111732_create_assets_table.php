<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration {
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create("assets", function (Blueprint $table) {
            $table->string("asset_id", 100);
            $table->string("file_name");
            $table->string("extension");
            $table->string("company_id", 100);
            $table->timestamps();

            $table->primary("asset_id");
            $table
                ->foreign("company_id")
                ->references("company_id")
                ->on("companies");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("assets");
    }
};
