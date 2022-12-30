<?php

namespace Database\Seeders;

use App\Models\Company;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Sequence;
use Illuminate\Database\Seeder;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        User::factory(5)
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
