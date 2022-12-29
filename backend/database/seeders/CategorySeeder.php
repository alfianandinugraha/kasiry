<?php

namespace Database\Seeders;

use App\Models\Category;
use App\Models\Company;
use Illuminate\Database\Eloquent\Factories\Sequence;
use Illuminate\Database\Seeder;

class CategorySeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Category::factory()
            ->count(20)
            ->state(
                new Sequence(
                    fn($sequence) => [
                        "company_id" => Company::all()->random(),
                    ]
                )
            )
            ->create();
    }
}
