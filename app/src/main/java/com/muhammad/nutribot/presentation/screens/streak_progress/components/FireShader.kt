package com.muhammad.nutribot.presentation.screens.streak_progress.components

import android.graphics.RuntimeShader
import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush

private const val FIRE_SHADER_SRC = """
uniform float2 iResolution;
uniform float  iTime;
uniform float  iHeight;
uniform float  iTurb;

float hash(float2 p) {
    p = fract(p * float2(127.1, 311.7));
    p += dot(p, p + 19.19);
    return fract(p.x * p.y);
}

float noise(float2 p) {
    float2 i = floor(p);
    float2 f = fract(p);
    float2 u = f * f * (3.0 - 2.0 * f);
    return mix(
        mix(hash(i),               hash(i + float2(1,0)), u.x),
        mix(hash(i + float2(0,1)), hash(i + float2(1,1)), u.x),
        u.y
    );
}

float fbm(float2 p) {
    float v = 0.0, a = 0.5;
    float2x2 rot = float2x2(0.8, 0.6, -0.6, 0.8);
    for (int i = 0; i < 5; i++) {
        v += a * noise(p);
        p  = rot * p * 2.1;
        a *= 0.5;
    }
    return v;
}

half4 main(float2 fragCoord) {
    float2 uv = fragCoord / iResolution;
    uv.y = 1.0 - uv.y;

    float2 q = float2(
        fbm(uv + float2(0.0, iTime * 0.4)),
        fbm(uv + float2(5.2, iTime * 0.35))
    );
    float2 r = float2(
        fbm(uv + iTurb * q + float2(1.7, 9.2) + iTime * 0.15),
        fbm(uv + iTurb * q + float2(8.3, 2.8) + iTime * 0.12)
    );
    float f = fbm(uv + iTurb * r);

    float shape = pow(1.0 - uv.y, 1.0 / iHeight) * 1.4;
    float flame  = clamp(f * shape, 0.0, 1.0);

    half3 col = mix(half3(0.0),  half3(0.6, 0.0, 0.0), flame);
    col        = mix(col,        half3(1.0, 0.3, 0.0),  flame * flame);
    col        = mix(col,        half3(1.0, 0.8, 0.1),  flame * flame * flame);
    col        = mix(col,        half3(1.0, 1.0, 0.9),  pow(flame, 6.0));

    return half4(col, flame);
}
"""

@Composable
fun FireShader(
    modifier: Modifier = Modifier,
    speed: Float = 3f,
    height: Float = 1f,
    turbulence: Float = 4f,
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        Box(modifier = modifier)
        return
    }

    val transition = rememberInfiniteTransition(label = "fire_shader")
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 60_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "time"
    )

    val runtimeShader = remember { RuntimeShader(FIRE_SHADER_SRC) }

    val brush = remember(time, speed, turbulence, height) {
        runtimeShader.setFloatUniform("iTime", time * speed * 0.001f)
        runtimeShader.setFloatUniform("iHeight", height)
        runtimeShader.setFloatUniform("iTurb", turbulence)
        ShaderBrush(runtimeShader)
    }

    Canvas(modifier = modifier) {
        runtimeShader.setFloatUniform("iResolution", size.width, size.height)
        drawRect(brush = brush)
    }
}