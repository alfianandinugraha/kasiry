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
        Schema::create("wholesale", function (Blueprint $table) {
            $table->string("wholesale_id", 100);
            $table->integer("total");
            $table->float("sell_price");
            $table->timestamps();

            $table->primary("wholesale_id");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("wholesale");
    }
};
