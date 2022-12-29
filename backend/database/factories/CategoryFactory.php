<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;
use Illuminate\Support\Str;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\Category>
 */
class CategoryFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition()
    {
        return [
            "category_id" => Str::random(10),
            "name" => fake()->randomElement([
                "Beverages",
                "Condiments",
                "Confections",
                "Dairy Products",
                "Grains/Cereals",
                "Meat/Poultry",
                "Produce",
                "Seafood",
            ]),
        ];
    }
}
