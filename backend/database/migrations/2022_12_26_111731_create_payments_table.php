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
        Schema::create("payments", function (Blueprint $table) {
            $table->string("payment_id", 100);
            $table->string("name");
            $table->string("user_id", 100);
            $table->timestamps();

            $table->primary("payment_id");
            $table
                ->foreign("user_id")
                ->references("user_id")
                ->on("users");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("payments");
    }
};
