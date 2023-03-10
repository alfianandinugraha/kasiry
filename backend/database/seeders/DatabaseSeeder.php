<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;

use App\Models\Category;
use App\Models\Company;
use App\Models\User;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    /**
     * Seed the application's database.
     *
     * @return void
     */
    public function run()
    {
        User::query()->delete();
        Category::query()->delete();
        Company::query()->delete();

        $this->call([
            CompanySeeder::class,
            UserSeeder::class,
            CategorySeeder::class,
        ]);
    }
}
