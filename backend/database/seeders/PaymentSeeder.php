<?php

namespace Database\Seeders;

use App\Models\Company;
use App\Models\Payment;
use Illuminate\Database\Eloquent\Factories\Sequence;
use Illuminate\Database\Seeder;

class PaymentSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        Payment::factory()
            ->count(10)
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
