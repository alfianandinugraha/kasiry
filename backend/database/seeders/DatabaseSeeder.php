<?php

namespace Database\Seeders;

// use Illuminate\Database\Console\Seeds\WithoutModelEvents;

use App\Models\Category;
use App\Models\Company;
use App\Models\Payment;
use App\Models\User;
use App\Models\Weight;
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
        Weight::query()->delete();
        User::query()->delete();
        Category::query()->delete();
        Payment::query()->delete();
        Company::query()->delete();

        $this->call([
            CompanySeeder::class,
            UserSeeder::class,
            CategorySeeder::class,
            PaymentSeeder::class,
            WeightSeeder::class,
        ]);
    }
}
