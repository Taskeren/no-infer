package cn.elytra.no_infer

import org.jetbrains.kotlin.analysis.api.KaExperimentalApi
import org.jetbrains.kotlin.analysis.api.KaSession
import org.jetbrains.kotlin.analysis.api.resolution.successfulFunctionCallOrNull
import org.jetbrains.kotlin.analysis.api.resolution.symbol
import org.jetbrains.kotlin.analysis.api.symbols.KaFunctionSymbol
import org.jetbrains.kotlin.analysis.api.symbols.typeParameters
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtCallElement

private val NO_INFER_ANNOTATION_CLASS_ID =
    ClassId(
        FqName("cn.elytra.no_infer"),
        FqName("NoInfer"),
        false,
    )

internal fun KaFunctionSymbol.isAnnotatedWithNoInfer(): Boolean = annotations.any { it.classId == NO_INFER_ANNOTATION_CLASS_ID }

@OptIn(KaExperimentalApi::class)
internal fun KaSession.resolveNoInferFunction(call: KtCallElement): KaFunctionSymbol? {
    val symbol = call.resolveToCall()?.successfulFunctionCallOrNull()?.symbol ?: return null
    return symbol.takeIf { it.typeParameters.isNotEmpty() && it.isAnnotatedWithNoInfer() }
}
