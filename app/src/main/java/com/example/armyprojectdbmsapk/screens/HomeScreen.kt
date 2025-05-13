package com.example.armyprojectdbmsapk.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.armyprojectdbmsapk.R

@Composable
fun ADMSApp(
    navController: NavHostController = rememberNavController(),
    onExploreDatabase: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ADMS",
                    style = TextStyle(
                        fontSize = 40.sp,
                        lineHeight = 43.2.sp,
                       // fontFamily = FontFamily(Font(R.font.preahvihear)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                    )
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(40.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_dp),
                        contentDescription = "Profile image",
                        contentScale = ContentScale.Crop, // better for profile pics
                        modifier = Modifier.fillMaxSize() // this fills the Box
                    )
                }
            }
            // Manage Your Forces Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column( modifier = Modifier
                    .padding(top = 210.dp)) {
                    Text(
                        text = "Manage \n\nYour\n\nForces",
                        style = TextStyle(
                            fontSize = 40.sp,
                            lineHeight = 43.2.sp,
                            // fontFamily = FontFamily(Font(R.font.playfair_display)),
                            fontWeight = FontWeight(900),
                            color = Color(0xFFB9BBBE),
                        ),
                        modifier = Modifier
                            .width(159.dp)
                            .height(274.dp)
                    )
                }

                Column {
                    // First Image Box
                    Box(
                        modifier = Modifier
                            .width(329.dp)
                            .height(190.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(color = Color(0xFFB3B3B3))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image1),
                            contentDescription = "Military image 1",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Second Image Box
                    Box(
                        modifier = Modifier
                            .width(329.dp)
                            .height(190.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(color = Color(0xFFB3B3B3))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = "Military image 2",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Third Image Box
                    Box(
                        modifier = Modifier
                            .width(329.dp)
                            .height(190.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(color = Color(0xFFB3B3B3))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img2),
                            contentDescription = "Military image 3",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp))
                        )
                    }
                }
            }

            // Explore Database Button
            Button(
                onClick = onExploreDatabase,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF6F5F5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "EXPLORE DATABASE",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(52.dp))

            // Purpose Card
            Card(
                modifier = Modifier
                    .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
                    .fillMaxWidth()
                    .height(115.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2A2A2A)
                ),
                shape = RoundedCornerShape(size = 38.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Our Purpose",
                        style = TextStyle(
                            fontSize = 24.sp,
                            lineHeight = 31.2.sp,
                       //     fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFAFAFAF),
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "To provide a reliable platform for managing military \ndata effectively.",
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 16.8.sp,
            //                fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                            fontWeight = FontWeight(700),
                            color = Color(0xFFF7FFFB),
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(52.dp))

            // Key Features Section
            Text(
                text = "Key Features",
                style = TextStyle(
                    fontSize = 56.sp,
                    lineHeight = 67.2.sp,
             //       fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(700),
                    color = Color(0xFFF7FFFB),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(67.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Explore the functionalities that enhance military operations.",
                style = TextStyle(
                    fontSize = 22.sp,
                    lineHeight = 30.8.sp,
        //            fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFD0DBD6),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Feature 1: Real-Time Data
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp)
                    .background(color = Color(0xFFB3B3B3), shape = RoundedCornerShape(size = 32.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.key_features_img1),
                    contentDescription = "Real-time data image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Real-Time Data",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
             //       fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFF7FFFB),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Access up-to-date information on soldiers and equipment instantly.",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 22.4.sp,
    //                fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFD0DBD6),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Feature 2: User-Friendly Interface
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp)
                    .background(color = Color(0xFFB3B3B3), shape = RoundedCornerShape(size = 32.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.key_features_img2),
                    contentDescription = "User-friendly interface image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "User-Friendly Interface",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
          //          fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFF7FFFB),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Navigate effortlessly through a clean and modern design.",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 22.4.sp,
             //       fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFD0DBD6),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Feature 3: Secure Access
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(224.dp)
                    .background(color = Color(0xFFB3B3B3), shape = RoundedCornerShape(size = 32.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.key_featues_img3),
                    contentDescription = "Secure access image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(32.dp))
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Secure Access",
                style = TextStyle(
                    fontSize = 18.sp,
                    lineHeight = 27.sp,
               //     fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFF7FFFB),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ensure data protection with robust security measures.",
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 22.4.sp,
            //        fontFamily = FontFamily(Font(R.font.ibm_plex_sans)),
                    fontWeight = FontWeight(400),
                    color = Color(0xFFD0DBD6),
                    textAlign = TextAlign.Center,
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}