<?php

namespace Database\Factories;

use App\Models\Restriction;
use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Restriction>
 */
class RestrictionFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition()
    {
        return [
            "restriction_id" => Str::random(10),
            "allowed" => [Restriction::SUPER],
        ];
    }
}
