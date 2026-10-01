package iad1tya.echo.music.ui.screens.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

private val Night = Color(0xFF100D25)
private val NightCard = Color(0xFF1C1738)
private val Purple = Color(0xFF9B7BFF)
private val Pink = Color(0xFFFF9BCB)
private val Lavender = Color(0xFFD9C9FF)
private val SoftWhite = Color(0xFFF7F2FF)
private val Muted = Color(0xFFB9B0D0)

private const val SUPPORT_UPI_ID = "gaan.support.sahnaz@fam"
private const val SUPPORT_PAYEE = "Gaan"

private enum class PaymentMethod {
    QR, UPI
}

private data class SupportBenefit(
    val title: String,
    val description: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val tint: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportScreen(
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    var paymentMethod by remember { mutableStateOf(PaymentMethod.QR) }
    var copied by remember { mutableStateOf(false) }

    val paymentUri = remember {
        Uri.Builder()
            .scheme("upi")
            .authority("pay")
            .appendQueryParameter("pa", SUPPORT_UPI_ID)
            .appendQueryParameter("pn", SUPPORT_PAYEE)
            .appendQueryParameter("cu", "INR")
            .build()
            .toString()
    }

    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(paymentUri) {
        qrBitmap = withContext(Dispatchers.Default) {
            generateSupportQr(paymentUri)
        }
    }

    val benefits = remember {
        listOf(
            SupportBenefit(
                "Ad-Free Experience",
                "Enjoy music without interruptions.",
                Icons.Outlined.Block,
                Pink
            ),
            SupportBenefit(
                "Regular Updates",
                "Help us keep Gaan improving.",
                Icons.Outlined.SystemUpdate,
                Lavender
            ),
            SupportBenefit(
                "New Features",
                "Support exciting features and ideas.",
                Icons.Outlined.AutoAwesome,
                Color(0xFFFFC879)
            ),
            SupportBenefit(
                "Reliable Servers",
                "Help maintain a smooth experience.",
                Icons.Outlined.Dns,
                Color(0xFF83D8D0)
            )
        )
    }

    Scaffold(
        containerColor = Night,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Support Gaan",
                        color = SoftWhite,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = SoftWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Night
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Night)
        ) {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 8.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(22.dp)
            ) {

                item {
                    SupportHero()
                }

                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SectionHeading(
                            title = "Why your support matters",
                            subtitle = "Every contribution helps Gaan grow."
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            benefits.chunked(2).forEach { pair ->
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    pair.forEach { benefit ->
                                        BenefitCard(
                                            benefit = benefit,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (pair.size == 1) {
                                        Spacer(Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    DonationCard(
                        paymentMethod = paymentMethod,
                        onMethodChange = { paymentMethod = it },
                        qrBitmap = qrBitmap,
                        copied = copied,
                        onCopyUpi = {
                            clipboard.setText(AnnotatedString(SUPPORT_UPI_ID))
                            copied = true
                        },
                        onPay = {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(paymentUri)
                            )
                            try {
                                context.startActivity(
                                    Intent.createChooser(
                                        intent,
                                        "Support Gaan with UPI"
                                    )
                                )
                            } catch (_: ActivityNotFoundException) {
                                // No compatible UPI application installed.
                            }
                        }
                    )
                }

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Made with love for music lovers.",
                            color = Muted,
                            fontSize = 13.sp
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            "Thank you for being part of Gaan ♡",
                            color = Pink,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SupportHero() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF34265D),
                        Color(0xFF20183F),
                        Color(0xFF17132F)
                    )
                )
            )
    ) {
        PixelSkyArtwork(
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "KEEP THE MUSIC ALIVE",
                color = Lavender,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(7.dp))

            Text(
                "Support Gaan",
                color = SoftWhite,
                fontSize = 29.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                "A little support makes a big difference.",
                color = Color(0xFFD6CBEF),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PixelSkyArtwork(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pixelSky")

    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(
                4500,
                easing = androidx.compose.animation.core.LinearEasing
            ),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "cloudMotion"
    )

    val starAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = androidx.compose.animation.core.infiniteRepeatable(
            animation = androidx.compose.animation.core.tween(1500),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        ),
        label = "starTwinkle"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Pixel moon
        drawRect(
            Color(0xFFFFE4A8),
            Offset(w * 0.77f, h * 0.12f),
            Size(28.dp.toPx(), 28.dp.toPx())
        )
        drawRect(
            Color(0xFF34265D),
            Offset(w * 0.81f, h * 0.10f),
            Size(15.dp.toPx(), 15.dp.toPx())
        )

        // Pixel stars
        val stars = listOf(
            Offset(.13f, .14f),
            Offset(.32f, .23f),
            Offset(.52f, .10f),
            Offset(.64f, .29f),
            Offset(.90f, .35f),
            Offset(.22f, .39f)
        )

        stars.forEach {
            drawRect(
                Color.White.copy(alpha = starAlpha),
                Offset(w * it.x, h * it.y),
                Size(3.dp.toPx(), 3.dp.toPx())
            )
        }

        // Moving pixel clouds
        drawPixelCloud(
            Offset(w * .18f + cloudOffset, h * .25f),
            0.8f
        )
        drawPixelCloud(
            Offset(w * .66f - cloudOffset, h * .43f),
            0.65f
        )

        // Ground
        drawRect(
            Color(0xFF30244C),
            Offset(0f, h * .76f),
            Size(w, h * .24f)
        )

        // Small pixel flowers
        drawPixelFlower(Offset(w * .12f, h * .79f), Pink)
        drawPixelFlower(Offset(w * .88f, h * .81f), Lavender)

        // Pixel cat
        drawPixelCat(Offset(w * .50f, h * .55f))
    }
}

private fun DrawScope.drawPixelCloud(
    center: Offset,
    scale: Float
) {
    val unit = 8.dp.toPx() * scale
    val cloud = Color(0xFFD8CCF4)

    val blocks = listOf(
        Offset(0f, unit),
        Offset(unit, 0f),
        Offset(unit * 2, 0f),
        Offset(unit * 3, unit),
        Offset(-unit, unit),
        Offset(0f, unit * 2),
        Offset(unit, unit * 2),
        Offset(unit * 2, unit * 2),
        Offset(unit * 3, unit * 2)
    )

    blocks.forEach {
        drawRect(
            cloud.copy(alpha = .75f),
            Offset(center.x + it.x, center.y + it.y),
            Size(unit, unit)
        )
    }
}

private fun DrawScope.drawPixelCat(center: Offset) {
    val p = 8.dp.toPx()
    val fur = Color(0xFF302443)
    val light = Color(0xFFFFD8E9)
    val eye = Color(0xFFFFD978)

    // Body
    drawRect(fur, Offset(center.x - p * 2, center.y), Size(p * 4, p * 4))

    // Head
    drawRect(fur, Offset(center.x - p * 2, center.y - p * 3), Size(p * 4, p * 3))

    // Ears
    drawRect(fur, Offset(center.x - p * 2, center.y - p * 4), Size(p, p))
    drawRect(fur, Offset(center.x + p, center.y - p * 4), Size(p, p))

    // Inner ears
    drawRect(light, Offset(center.x - p * 1.7f, center.y - p * 3.7f), Size(p * .45f, p * .45f))
    drawRect(light, Offset(center.x + p * 1.25f, center.y - p * 3.7f), Size(p * .45f, p * .45f))

    // Eyes
    drawRect(eye, Offset(center.x - p, center.y - p * 2), Size(p * .45f, p * .65f))
    drawRect(eye, Offset(center.x + p * .6f, center.y - p * 2), Size(p * .45f, p * .65f))

    // Nose
    drawRect(Pink, Offset(center.x - p * .25f, center.y - p), Size(p * .5f, p * .35f))

    // Tail
    drawRect(fur, Offset(center.x + p * 2, center.y + p * 2), Size(p, p))
    drawRect(fur, Offset(center.x + p * 3, center.y + p), Size(p, p))
}

private fun DrawScope.drawPixelFlower(
    center: Offset,
    petal: Color
) {
    val p = 5.dp.toPx()

    drawRect(petal, Offset(center.x - p, center.y - p), Size(p, p))
    drawRect(petal, Offset(center.x + p, center.y - p), Size(p, p))
    drawRect(petal, Offset(center.x - p, center.y + p), Size(p, p))
    drawRect(petal, Offset(center.x + p, center.y + p), Size(p, p))
    drawRect(Color(0xFFFFD978), center, Size(p, p))

    drawRect(
        Color(0xFF7ACB9B),
        Offset(center.x, center.y + p * 2),
        Size(p, p * 3)
    )
}

@Composable
private fun SectionHeading(
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            color = SoftWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            subtitle,
            color = Muted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun BenefitCard(
    benefit: SupportBenefit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(132.dp)
            .clip(RoundedCornerShape(19.dp))
            .background(NightCard)
            .border(
                1.dp,
                Color.White.copy(alpha = .06f),
                RoundedCornerShape(19.dp)
            )
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(benefit.tint.copy(alpha = .13f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                benefit.icon,
                contentDescription = null,
                tint = benefit.tint,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            benefit.title,
            color = SoftWhite,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp
        )

        Text(
            benefit.description,
            color = Muted,
            fontSize = 10.sp,
            lineHeight = 14.sp
        )
    }
}

@Composable
private fun DonationCard(
    paymentMethod: PaymentMethod,
    onMethodChange: (PaymentMethod) -> Unit,
    qrBitmap: Bitmap?,
    copied: Boolean,
    onCopyUpi: () -> Unit,
    onPay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF29204C),
                        Color(0xFF1B1735)
                    )
                )
            )
            .border(
                1.dp,
                Purple.copy(alpha = .25f),
                RoundedCornerShape(24.dp)
            )
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Purple.copy(alpha = .17f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = null,
                tint = Pink,
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Support the project",
            color = SoftWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Text(
            "If you enjoy using Gaan, consider supporting its development.",
            color = Muted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Night.copy(alpha = .7f))
                .padding(4.dp)
        ) {
            PaymentTab(
                title = "QR Code",
                selected = paymentMethod == PaymentMethod.QR,
                modifier = Modifier.weight(1f),
                onClick = { onMethodChange(PaymentMethod.QR) }
            )

            PaymentTab(
                title = "UPI Payment",
                selected = paymentMethod == PaymentMethod.UPI,
                modifier = Modifier.weight(1f),
                onClick = { onMethodChange(PaymentMethod.UPI) }
            )
        }

        Spacer(Modifier.height(18.dp))

        AnimatedContent(
            targetState = paymentMethod,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 5 }) togetherWith
                    (fadeOut() + slideOutVertically { -it / 5 })
            },
            label = "paymentContent"
        ) { method ->
            when (method) {
                PaymentMethod.QR -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .shadow(8.dp, RoundedCornerShape(18.dp))
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFFFFF9FF))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (qrBitmap != null) {
                                androidx.compose.foundation.Image(
                                    bitmap = qrBitmap.asImageBitmap(),
                                    contentDescription = "Gaan UPI payment QR code",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                CircularProgressIndicator(
                                    color = Purple,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            "Scan using any UPI app",
                            color = SoftWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )

                        Text(
                            "Google Pay • PhonePe • Paytm • BHIM",
                            color = Muted,
                            fontSize = 10.sp
                        )
                    }
                }

                PaymentMethod.UPI -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Outlined.AccountBalanceWallet,
                            contentDescription = null,
                            tint = Lavender,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "Pay directly using UPI",
                            color = SoftWhite,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "Your payment app will open with Gaan's payment details.",
                            color = Muted,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = onCopyUpi,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(13.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Lavender
                            )
                        ) {
                            Icon(
                                if (copied) Icons.Outlined.Check else Icons.Outlined.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.size(17.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(if (copied) "UPI ID copied" else "Copy UPI ID")
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        Button(
            onClick = onPay,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Purple,
                contentColor = Color.White
            )
        ) {
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(9.dp))
            Text(
                "Support Gaan",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            "Every contribution is appreciated. Thank you!",
            color = Muted,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun PaymentTab(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(11.dp))
            .background(
                if (selected) Purple.copy(alpha = .25f)
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            color = if (selected) SoftWhite else Muted,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

private fun generateSupportQr(content: String): Bitmap? {
    return try {
        val hints = hashMapOf<EncodeHintType, Any>(
            EncodeHintType.MARGIN to 4,
            EncodeHintType.ERROR_CORRECTION to
                com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M
        )

        val matrix = QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            640,
            640,
            hints
        )

        val bitmap = Bitmap.createBitmap(
            matrix.width,
            matrix.height,
            Bitmap.Config.ARGB_8888
        )

        val dark = AndroidColor.rgb(35, 24, 68)
        val light = AndroidColor.rgb(255, 249, 255)

        for (x in 0 until matrix.width) {
            for (y in 0 until matrix.height) {
                bitmap.setPixel(
                    x,
                    y,
                    if (matrix[x, y]) dark else light
                )
            }
        }

        bitmap
    } catch (_: Exception) {
        null
    }
}
