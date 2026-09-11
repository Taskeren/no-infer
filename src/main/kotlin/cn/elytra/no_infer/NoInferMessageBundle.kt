package cn.elytra.no_infer

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey
import java.util.function.Supplier

private const val BUNDLE = "messages.NoInferMessageBundle"

internal object NoInferMessageBundle {
    private val instance = DynamicBundle(NoInferMessageBundle::class.java, BUNDLE)

    @JvmStatic
    fun message(
        key:
            @PropertyKey(resourceBundle = BUNDLE)
            String,
        vararg params: Any?,
    ): String = instance.getMessage(key, *params)

    @JvmStatic
    fun lazyMessage(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any?,
    ): Supplier<String> = instance.getLazyMessage(key, *params)
}
