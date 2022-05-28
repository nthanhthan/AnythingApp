package com.example.anythingapp

import RoundIconButton
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.anythingapp.components.InputField
import com.example.anythingapp.ui.theme.AnythingAppTheme
import com.example.anythingapp.util.calculateTotalPerPerson
import com.example.anythingapp.util.calculateTotalTip
import com.example.totalbill.data.CounterViewModel

@Composable
fun HomeScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
            ){
        val counter= remember {
            mutableStateOf(0)
        }
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
            color = MaterialTheme.colors.background) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${counter.value}",
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 35.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(modifier = Modifier.height(130.dp))
                CreateCircle(counter = counter.value){
                        newValue->counter.value=newValue
                }
            }
        }
    }
}
@Composable
fun CreateCircle(counter: Int=0,countUp: (Int)->Unit){
    Card(modifier = Modifier
        .padding(3.dp)
        .size(105.dp)
        .clickable {
            countUp(counter+1)
        }
        .size(105.dp),
        shape = CircleShape,
        elevation = 4.dp) {
        Box(contentAlignment = Alignment.Center){
            Text(text = "Tap")
        }
    }

}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MusicScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ){
        MyApp {
            TipCalculator()
        }
    }
}
@Composable
fun MyApp(content: @Composable () -> Unit) {
    AnythingAppTheme() {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun TipCalculator() {
    Surface( modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
        color = MaterialTheme.colors.background) {
        Column() {
            MainContent()
        }
    }
}

@Preview
@Composable
fun TopHeader(totalPerPers: Double = 0.0) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .padding(12.dp)
        .clip(shape = CircleShape.copy(all = CornerSize(12.dp))), color = Color(0xFFe9d7f7)) {
        Column(modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text(text = "Total Per Person", style = MaterialTheme.typography.subtitle1)
            val total = "%.2f".format(totalPerPers)
            Text(text = "\$$total", style = MaterialTheme.typography.h4)


        }

    }

}

@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent() {
    val splitBy = remember {
        mutableStateOf(1)
    }

    val totalTipAmt = remember {
        mutableStateOf(0.0)
    }

    val totalPerPerson = remember {
        mutableStateOf(0.0)
    }
    BillForm(splitByState = splitBy,
        tipAmountState = totalTipAmt,
        totalPerPersonState = totalPerPerson

    ) {

    }
}


@ExperimentalComposeUiApi
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {},
) {

    var sliderPosition by remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPosition * 100).toInt()
    val totalBill = rememberSaveable { mutableStateOf("") } //or just remember {}
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(totalBill.value) {
        totalBill.value.trim().isNotEmpty()
    }

    TopHeader(totalPerPers = totalPerPersonState.value)

    Surface(modifier = Modifier
        .padding(2.dp)
        .fillMaxWidth(),
        shape = CircleShape.copy(all = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)) {

        Column(modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start) {

            InputField(
                valueState = totalBill, labelId = "Enter Bill",
                enabled = true,
                onAction = KeyboardActions {
                    //The submit button is disabled unless the inputs are valid. wrap this in if statement to accomplish the same.
                    if (!valid) return@KeyboardActions
                    onValChange(totalBill.value.trim())
                    keyboardController?.hide()
                },
            )

            if (valid) {
                Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.Start) {
                    Text(text = "Split",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(120.dp))

                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End) {

                        RoundIconButton(imageVector = Icons.Default.ArrowBack, onClick = {
                            splitByState.value =
                                if (splitByState.value > 1) splitByState.value - 1 else 1
                            totalPerPersonState.value =
                                calculateTotalPerPerson(totalBill = totalBill.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercent = tipPercentage)
                        })

                        Text(text = "${splitByState.value}",
                            Modifier
                                .align(alignment = Alignment.CenterVertically)
                                .padding(start = 9.dp, end = 9.dp))
                        RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                            if (splitByState.value < range.last) {
                                splitByState.value = splitByState.value + 1

                                totalPerPersonState.value =
                                    calculateTotalPerPerson(totalBill = totalBill.value.toDouble(),
                                        splitBy = splitByState.value,
                                        tipPercent = tipPercentage)
                            }
                        })

                    }
                }
                //Tip Row
                Row(modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.End) {
                    Text(text = "Tip",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically))

                    Spacer(modifier = Modifier.width(200.dp))

                    Text(text = "$${tipAmountState.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically))

                }
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(text = "$tipPercentage %")
                    Spacer(modifier = Modifier.height(14.dp))
                    Slider(value = sliderPosition,
                        onValueChange = { newVal ->
                            sliderPosition = newVal
                            tipAmountState.value =
                                calculateTotalTip(totalBill = totalBill.value.toDouble(),
                                    tipPercent = tipPercentage)

                            totalPerPersonState.value =
                                calculateTotalPerPerson(totalBill = totalBill.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercent = tipPercentage)
                            Log.d("Slider",
                                "Total Bill-->: ${"%.2f".format(totalPerPersonState.value)}")

                        },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        steps = 5,
                        onValueChangeFinished = {
                            Log.d("Finished", "BillForm: $tipPercentage")
                            //This is were the calculations should happen!
                        })

                }

            }


        }
    }//end isValid

}


@Composable
fun ScreenDemo(model: CounterViewModel) {
    Column(modifier = Modifier.padding(14.dp)) {
        Demo("This is ${model.getCounter()}", counterViewModel = model) {
            if (it) {
                model.increaseCounter()
            } else {
                model.decreaseCounter()
            }
        }

        if (model.getCounter() > 12) {

            Text(text = "I love this so much!!")


        }
    }



}

@Composable
fun Demo(
    text: String,
    counterViewModel: CounterViewModel,
    onclick: (Boolean) -> Unit = {},
) {
    val isVal = remember {
        mutableStateOf(false)
    }
    Column(verticalArrangement = Arrangement.Center) {
        var isRange by remember {
            mutableStateOf(false)
        }
        isRange = counterViewModel.getCounter() == 12
        Text(text = text, color = if (isRange) Color.Red else Color.LightGray)


        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    isVal.value = true
                    onclick(isVal.value)
                },
            ) {

                BasicText(text = "Add 1")
            }

            Button(
                onClick = {
                    isVal.value = false
                    onclick(isVal.value)
                },
            ) {

                BasicText(text = "Minus 1")
            }

        }

    }


}


@Composable
private fun TipSlider(
    modifier: Modifier = Modifier,
    sliderState: MutableState<Float>,
    totalTipState: MutableState<Double>,
    totalBillState: MutableState<String>,
) {
    val tipPercentage = (sliderState.value.toInt())

    val percentage = buildAnnotatedString {
        withStyle(style = SpanStyle(fontSize = 32.sp)) { append(tipPercentage.toString()) }
        append(" %")
    }
    //Slider
    Column(verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = percentage.toString())
        Spacer(modifier = Modifier.height(14.dp))
        Slider(value = sliderState.value,
            onValueChange = {
                sliderState.value = it
                totalTipState.value = calculateTotalTip(totalBill = totalBillState.value.toDouble(),
                    tipPercent = tipPercentage)
            },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            steps = 5,
            valueRange = (0f..100f))

    }

}
@Composable
fun MovieScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ){
        Text(text = "Movie View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Composable
fun BooksScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ){
        Text(text = "Book View",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 25.sp
        )
    }
}

@Composable
fun ProfileScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.colorPrimaryDark))
            .wrapContentSize(Alignment.Center)
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            color = MaterialTheme.colors.background
        ) {
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(390.dp)
                    .padding(12.dp),
                shape = RoundedCornerShape(corner = CornerSize(15.dp)),
                elevation = 4.dp
            ) {
                val status = remember {
                    mutableStateOf(false)
                }
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.height(300.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CreateImageProfile()
                        CreateInfo()
                        Button(
                            onClick = {
                                status.value = !status.value
                            },
                            modifier = Modifier.padding(5.dp)
                        ) {
                            Text(text = "Portfolio", style = MaterialTheme.typography.button)

                        }
                        Divider()
                        if (status.value)
                            Portfolio(data = listOf("Project 1", "Project 2", "Project 3"))
                    }

                }
            }
        }
    }
}
@Composable
fun CreateInfo(){
    Column(modifier = Modifier.padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nguyen Thanh Than",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primaryVariant)
        Text(text = "DHBK-DHDN",
            modifier=Modifier.padding(3.dp))
        Text(text = "22-07-2001",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(3.dp)
        )
    }
}
@Composable
fun CreateImageProfile(){
    androidx.compose.material.Surface(
        modifier = Modifier
            .size(150.dp)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp,color = Color.LightGray),
        elevation = 4.dp,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f)
    ) {
        Image(painter = painterResource(id =R.drawable.ic_baseline_person_24 ),
            contentDescription = "profile image",
            modifier = Modifier.size(135.dp),
            contentScale = ContentScale.Crop
        )
    }
}
@Composable
fun Portfolio(data: List<String>){
    LazyColumn{
        items(data){
                item ->
            Card(
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth(),
                shape = RectangleShape,
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(MaterialTheme.colors.surface)
                ) {
                    CreateImageProfile()
                    Column(
                        modifier = Modifier
                            .padding(7.dp)
                            .align(alignment = Alignment.CenterVertically)
                    ) {
                        Text(text = item, fontWeight = FontWeight.Bold)
                        Text(text = "A great project", style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    }
}