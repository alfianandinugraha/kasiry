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
        Schema::create("restrictions", function (Blueprint $table) {
            $table->string("restriction_id", 100);
            $table->json("allowed");
            $table->string("user_id", 100);
            $table->timestamps();

            $table->primary("restriction_id");
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
        Schema::dropIfExists("restrictions");
    }
};
