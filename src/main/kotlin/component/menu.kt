package component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ItemMenu(title: String, onclick: () -> Unit) {
    Box(modifier = Modifier
        .padding(vertical = 2.dp, horizontal = 4.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .clickable { onclick() }
        .background(MaterialTheme.colors.secondaryVariant)
        .padding(10.dp)

    ) {
        Text(text = title, color = MaterialTheme.colors.onBackground)
    }
}