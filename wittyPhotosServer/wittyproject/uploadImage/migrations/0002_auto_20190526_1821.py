# Generated by Django 2.1 on 2019-05-26 09:21

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('uploadImage', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='MyImage',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('model_pic', models.ImageField(default='none/no-img.jpg', upload_to='media')),
            ],
        ),
        migrations.DeleteModel(
            name='NewImage',
        ),
    ]
