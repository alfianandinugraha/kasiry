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
        Schema::create("companies", function (Blueprint $table) {
            $table->string("company_id", 100);
            $table->string("name");
            $table->string("address");
            $table->string("phone");
            $table->string("user_id", 100);
            $table->timestamps();

            $table->primary("company_id");
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
        Schema::dropIfExists("companies");
    }
};
