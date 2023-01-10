<?php

namespace Database\Seeders;

use App\Models\Company;
use App\Models\User;
use Illuminate\Database\Eloquent\Factories\Sequence;
use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Str;

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

        $userId = Str::random(10);
        $companyId = Str::random(10);

        User::factory()->create([
            "user_id" => $userId,
            "name" => "Alfian Andi",
            "email" => "admin@gmail.com",
            "phone" => "081312451829",
            "password" => Hash::make("admin123"),
            "abilities" => [
                "employee" => true,
                "transaction" => true,
                "product" => true,
            ],
        ]);

        Company::factory()->create([
            "company_id" => $companyId,
            "name" => "Toko Sukses",
            "address" => "Sleman, Yogyakarta",
            "phone" => fake("ID")->phoneNumber(),
        ]);

        User::query()
            ->find($userId)
            ->update([
                "company_id" => $companyId,
            ]);
    }
}
