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
        Schema::create("products", function (Blueprint $table) {
            $table->string("product_id", 100);
            $table->string("name");
            $table->string("description");
            $table->integer("stock");
            $table->float("buy_price");
            $table->float("sell_price");
            $table->string("barcode");
            $table->string("picture_id", 100);
            $table->string("company_id", 100);
            $table->string("category_id", 100);
            $table->timestamps();

            $table->primary("product_id");
            $table
                ->foreign("company_id")
                ->references("company_id")
                ->on("companies");
            $table
                ->foreign("category_id")
                ->references("category_id")
                ->on("categories");
            $table
                ->foreign("picture_id")
                ->references("asset_id")
                ->on("assets");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("products");
    }
};
