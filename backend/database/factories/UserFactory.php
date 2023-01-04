<?php

namespace Database\Factories;

use App\Models\Ability;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\User>
 */
class UserFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition()
    {
        return [
            "user_id" => Str::random(10),
            "name" => fake()->name(),
            "phone" => fake()->phoneNumber(),
            "abilities" => [
                "employee" => true,
                "product" => true,
                "transaction" => true,
            ],
            "email" => fake()
                ->unique()
                ->safeEmail(),
            "password" =>
                '$2y$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', // password
        ];
    }

    /**
     * Indicate that the model's email address should be unverified.
     *
     * @return static
     */
    public function unverified()
    {
        return $this->state(
            fn(array $attributes) => [
                "email_verified_at" => null,
            ]
        );
    }
}
