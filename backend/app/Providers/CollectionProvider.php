<?php

namespace App\Providers;

use Illuminate\Database\Eloquent\Collection;
use Illuminate\Support\Arr;
use Illuminate\Support\Str;
use Illuminate\Support\ServiceProvider;

class CollectionProvider extends ServiceProvider
{
    /**
     * Register services.
     *
     * @return void
     */
    public function register()
    {
        //
    }

    /**
     * Bootstrap services.
     *
     * @return void
     */
    public function boot()
    {
        function camelKeys($arr)
        {
            return $arr->mapWithKeys(function ($item, $key) use ($arr) {
                if (Arr::accessible($item)) {
                    return [
                        Str::camel($key) => camelKeys(collect($arr->get($key))),
                    ];
                }
                return [
                    Str::camel($key) => $item,
                ];
            });
        }

        Collection::macro("camelKeys", function () {
            return camelKeys($this);
        });
    }
}
