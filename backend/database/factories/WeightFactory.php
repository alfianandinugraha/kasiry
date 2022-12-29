<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Weight>
 */
class WeightFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition()
    {
        return [
            "weight_id" => Str::random(10),
            "name" => fake()->randomElement([
                "kg",
                "lb",
                "gram",
                "oz",
                "ton",
                "mg",
                "g",
                "m",
                "unit",
            ]),
        ];
    }
}
