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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import iad1tya.echo.music.R

private val Pink = Color(0xFFFF9BCB)
private val Lavender = Color(0xFFD9C9FF)

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
    val scope = rememberCoroutineScope()
    val colors = MaterialTheme.colorScheme
    val snackbarHostState = remember { SnackbarHostState() }

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

    LaunchedEffect(copied) {
        if (copied) {
            delay(1800)
            copied = false
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
        containerColor = colors.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Support Gaan",
                        color = colors.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = colors.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.surface,
                    scrolledContainerColor = colors.surface
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(colors.surfaceContainerLow, colors.background)
                    )
                )
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
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "No compatible UPI app found on this device"
                                    )
                                }
                            }
                        }
                    )
                }

                item {
                    val colors = MaterialTheme.colorScheme

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Made with love for music lovers.",
                            color = colors.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyMedium
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
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 290.dp, max = 340.dp)
            .clip(RoundedCornerShape(28.dp))
            .border(1.dp, Pink.copy(alpha = 0.28f), RoundedCornerShape(28.dp))
    ) {
        // Gradient background instead of image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF29204C),
                            Color(0xFF1B1735)
                        )
                    )
                )
        )

        // Gradient Box removed


        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(R.mipmap.ic_launcher_round),
                contentDescription = "Gaan App Icon",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                "KEEP THE MUSIC ALIVE",
                color = colors.primaryContainer,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(7.dp))

            Text(
                "Support Gaan",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                "A little support makes a big difference.",
                color = Color.White.copy(alpha = 0.84f),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SectionHeading(
    title: String,
    subtitle: String
) {
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            color = colors.onBackground,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            subtitle,
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun BenefitCard(
    benefit: SupportBenefit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .heightIn(min = 142.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surfaceContainer)
            .border(
                1.dp,
                colors.outlineVariant.copy(alpha = 0.45f),
                RoundedCornerShape(20.dp)
            )
            .padding(15.dp),
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
            color = colors.onSurface,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            benefit.description,
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
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
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        colors.surfaceContainerHighest,
                        colors.surfaceContainerHigh
                    )
                )
            )
            .border(
                1.dp,
                colors.primary.copy(alpha = .34f),
                RoundedCornerShape(24.dp)
            )
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(colors.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = null,
                tint = colors.onPrimaryContainer,
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "Support the project",
            color = colors.onSurface,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Text(
            "If you enjoy using Gaan, consider supporting its development.",
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(Modifier.height(18.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(colors.surface.copy(alpha = .78f))
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
                                .background(Color.White)
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
                                    color = colors.primary,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            "Scan using any UPI app",
                            color = colors.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Text(
                            "Google Pay • PhonePe • Paytm • BHIM",
                            color = colors.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
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
                            tint = colors.primary,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        Text(
                            "Pay directly using UPI",
                            color = colors.onSurface,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            "Your payment app will open with Gaan's payment details.",
                            color = colors.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(14.dp))

                        OutlinedButton(
                            onClick = onCopyUpi,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(13.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = colors.primary
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
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        ) {
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(9.dp))
            Text(
                if (paymentMethod == PaymentMethod.QR) "OPEN UPI APP" else "PAY WITH UPI",
                fontWeight = FontWeight.Bold,
                color = colors.onPrimary
            )
        }

        Spacer(Modifier.height(10.dp))

        Text(
            "Every contribution is appreciated. Thank you!",
            color = colors.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
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
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(11.dp))
            .background(
                if (selected) colors.primaryContainer
                else Color.Transparent
            )
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab
            )
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            title,
            color = if (selected) colors.onPrimaryContainer else colors.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
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
