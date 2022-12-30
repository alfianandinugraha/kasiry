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
        Schema::create("users", function (Blueprint $table) {
            $table->string("user_id", 100);
            $table->string("name");
            $table->string("email");
            $table->string("phone");
            $table->string("password");
            $table->string("company_id", 100)->nullable();
            $table->string("restriction_id", 100);
            $table->timestamps();

            $table->primary("user_id");
            $table
                ->foreign("company_id")
                ->references("company_id")
                ->on("companies");

            $table
                ->foreign("restriction_id")
                ->references("restriction_id")
                ->on("restrictions");
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists("user");
    }
};
