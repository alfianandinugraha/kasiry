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
        Schema::create("categories", function (Blueprint $table) {
            $table->string("category_id", 100);
            $table->string("name");
            $table->string("company_id", 100);
            $table->timestamps();

            $table->primary("category_id");
            $table
                ->foreign("company_id")
                ->references("company_id")
                ->onDelete("CASCADE")
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
        Schema::dropIfExists("categories");
    }
};
