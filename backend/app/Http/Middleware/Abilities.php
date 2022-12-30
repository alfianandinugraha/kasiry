<?php

namespace App\Http\Middleware;

use App\Models\Ability;
use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Str;

class Abilities
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure(\Illuminate\Http\Request): (\Illuminate\Http\Response|\Illuminate\Http\RedirectResponse)  $next
     * @return \Illuminate\Http\Response|\Illuminate\Http\RedirectResponse
     */
    public function handle(Request $request, Closure $next, $args)
    {
        $user = $request->user();
        $abilities = $user->abilities;

        $isSuper = Ability::isSuper($abilities);
        if ($isSuper) {
            return $next($request);
        }

        $required = Str::of($args)->explode("|");
        $isAllowed = Ability::allowed($required, $abilities);

        if (!$isAllowed) {
            abort(403);
        }

        return $next($request);
    }
}
