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
        Schema::create("transaction_products", function (Blueprint $table) {
            $table->string("transaction_product_id", 100);
            $table->string("transaction_id", 100);
            $table->string("name");
            $table->integer("quantity");
            $table->float("buy_price");
            $table->float("sell_price");
            $table->string("barcode");
            $table->string("unit_name", 100);
            $table->timestamps();

            $table->primary("transaction_product_id");
            $table
                ->foreign("transaction_id")
                ->references("transaction_id")
                ->on("transactions");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("transaction_products");
    }
};
